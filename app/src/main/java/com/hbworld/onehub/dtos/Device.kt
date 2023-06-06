package com.hbworld.onehub.dtos

import java.net.InetAddress


//Device(ipAddress=/192.168.1.5, name=Google-Nest-Hub-#886C, openPorts=[8009, 49191, 10001], serviceTypes=[._googlecast._tcp, ._meshcop._udp, ._googlezone._tcp], txtRecords=null)
//Device(ipAddress=/192.168.1.8, name=doordarshan, openPorts=[8009, 6466], serviceTypes=[._googlecast._tcp, ._androidtvremote2._tcp], txtRecords=null)
//Device(ipAddress=/192.168.1.7, name=Hemant’s MacBook Air, openPorts=[7000, 631, 58802], serviceTypes=[._airplay._tcp, ._raop._tcp, ._ipps._tcp, ._ipp._tcp, ._companion-link._tcp], txtRecords=null)

data class Device(
    val ipAddress: InetAddress,
    var name: String,
    val openPorts: MutableSet<Int>,
    val serviceTypes: MutableSet<String>,
    val txtRecords: MutableMap<String, ByteArray>? = null
) {
}