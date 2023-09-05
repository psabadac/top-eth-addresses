import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.Executors

fun main() = runBlocking {
    val dispatcher = Executors
        .newFixedThreadPool(20)
        .asCoroutineDispatcher()

    val addresses = File("src/main/resources/top_eth_addresses.txt").useLines { it.toList() }

    while (true) {
        launch(dispatcher) {
            val derivedAddress = Utils.createCredentials()
            if (derivedAddress.address in addresses) {
                println(derivedAddress.mnemonic)
            }
        }
    }
}