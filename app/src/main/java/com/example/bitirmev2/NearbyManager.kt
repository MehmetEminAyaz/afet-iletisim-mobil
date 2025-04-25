package com.example.bitirmev2.nearby

import android.content.Context
import android.util.Log
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.model.HelpMessage
import com.example.bitirmev2.notifications.NotificationHelper
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

object NearbyManager {

    private const val SERVICE_ID = "com.example.bitirmev2"
    private val strategy = Strategy.P2P_STAR
    lateinit var connectionsClient: ConnectionsClient
    val connectedPeers = mutableSetOf<String>()
    lateinit var appContext: Context

    // 🧠 Bildirim grubu için mesaj sayacı
    private var receivedMessageCount = 0

    fun init(context: Context) {
        appContext = context.applicationContext
        connectionsClient = Nearby.getConnectionsClient(context)
        Log.d("Nearby", "Nearby initialized.")
    }

    fun startAdvertising(deviceName: String) {
        val options = AdvertisingOptions.Builder().setStrategy(strategy).build()

        connectionsClient.startAdvertising(
            deviceName,
            SERVICE_ID,
            connectionLifecycleCallback,
            options
        ).addOnSuccessListener {
            Log.d("Nearby", "Advertising started successfully as $deviceName")
        }.addOnFailureListener {
            Log.e("Nearby", "Advertising failed: ${it.message}")
        }
    }

    fun startDiscovery() {
        val options = DiscoveryOptions.Builder().setStrategy(strategy).build()

        connectionsClient.startDiscovery(
            SERVICE_ID,
            endpointDiscoveryCallback,
            options
        ).addOnSuccessListener {
            Log.d("Nearby", "Discovery started successfully.")
        }.addOnFailureListener {
            Log.e("Nearby", "Discovery failed: ${it.message}")
        }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Log.d("Nearby", "Connection initiated with $endpointId")
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                connectedPeers.add(endpointId)
                Log.d("Nearby", "Connection successful with $endpointId")

                // 🔔 Bildirim: bağlantı başarılı
                NotificationHelper.showConnectionStatus(
                    context = appContext,
                    statusMessage = "Cihaz bağlandı: $endpointId"
                )
            } else {
                Log.e("Nearby", "Connection failed with $endpointId: ${result.status.statusMessage}")
            }
        }

        override fun onDisconnected(endpointId: String) {
            connectedPeers.remove(endpointId)
            Log.d("Nearby", "Disconnected from $endpointId")

            // 🔔 Bildirim: bağlantı koptu
            NotificationHelper.showConnectionStatus(
                context = appContext,
                statusMessage = "Cihaz bağlantısı kesildi: $endpointId"
            )
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Log.d("Nearby", "Endpoint found: $endpointId, name: ${info.endpointName}")
            connectionsClient.requestConnection("Cihaz", endpointId, connectionLifecycleCallback)
        }

        override fun onEndpointLost(endpointId: String) {
            Log.w("Nearby", "Endpoint lost: $endpointId")
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            payload.asBytes()?.let {
                val json = String(it)
                try {
                    val message = Gson().fromJson(json, HelpMessage::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val existing = LocalMessageRepository.getById(message.id)
                        if (existing == null) {
                            LocalMessageRepository.insert(message)
                            Log.d("Nearby", "Yeni mesaj alındı ve kaydedildi: ${message.id}")

                            // 🔔 Bireysel bildirim
                            NotificationHelper.showSingleMessageNotification(
                                context = appContext,
                                sender = message.senderName,
                                type = message.type.name,
                                address = message.address
                            )

                            // 🔔 Grup bildirimi (2 veya daha fazla mesaj geldiyse)
                            receivedMessageCount++
                            if (receivedMessageCount >= 2) {
                                NotificationHelper.showGroupedSummary(appContext, receivedMessageCount)
                            }

                            // Sayaç 1.5 saniyede bir sıfırlanır
                            launch {
                                delay(1500)
                                receivedMessageCount = 0
                            }
                        } else {
                            Log.i("Nearby", "Mesaj zaten var (ID: ${message.id}), tekrar eklenmedi.")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Nearby", "Mesaj parse edilirken hata: ${e.message}")
                }
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d("Nearby", "Payload transfer update from $endpointId: $update")
        }
    }

    fun sendMessageToPeers(message: HelpMessage) {
        val json = Gson().toJson(message)
        val payload = Payload.fromBytes(json.toByteArray())

        if (connectedPeers.isEmpty()) {
            Log.w("Nearby", "Hiç bağlı cihaz yok, mesaj gönderilemiyor.")
        }

        for (peer in connectedPeers) {
            connectionsClient.sendPayload(peer, payload)
                .addOnSuccessListener {
                    Log.d("Nearby", "Mesaj başarıyla gönderildi: ${message.id} -> $peer")
                }
                .addOnFailureListener {
                    Log.e("Nearby", "Mesaj gönderimi başarısız: $peer - ${it.message}")
                }
        }
    }
}
