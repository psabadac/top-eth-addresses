import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.MnemonicUtils
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.security.SecureRandom

object Utils {
    fun createCredentials(
        mnemonic: String = generateMnemonic()
    ): DerivedAddress {
        // https://stackoverflow.com/questions/52107608/how-to-use-mnemonic-to-recovery-my-ethereum-wallet
        val seed = MnemonicUtils.generateSeed(mnemonic, "")
        val masterKeypair = Bip32ECKeyPair.generateKeyPair(seed)
        val path = getPathForIndex()
        val childKeypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)
        val credentials = Credentials.create(childKeypair)

        return DerivedAddress(
            mnemonic = mnemonic,
            privateKey = privateKeyToString(credentials.ecKeyPair.privateKey),
            address = credentials.address
        )
    }

    private fun generateMnemonic(): String {
        val initialEntropy = ByteArray(16)
        SecureRandom().nextBytes(initialEntropy)
        return MnemonicUtils.generateMnemonic(initialEntropy)
    }

    private fun getPathForIndex(index: Int = 0) = intArrayOf(
        44 or Bip32ECKeyPair.HARDENED_BIT,
        60 or Bip32ECKeyPair.HARDENED_BIT,
        0 or Bip32ECKeyPair.HARDENED_BIT,
        0,
        index
    )

    private fun privateKeyToString(privateKey: BigInteger): String =
        Numeric.toHexStringWithPrefixZeroPadded(privateKey, Keys.PRIVATE_KEY_LENGTH_IN_HEX)
}