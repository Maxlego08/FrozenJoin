import org.junit.jupiter.api.Test

class Test {

    init {
        testRegex()
    }

    @Test
    fun testRegex() {
        val messages = listOf(
                "[BROADCAST] &8[&a+&8] &b%player_name%",
                "[CENTERMESSAGE] #<0cf797>Smexy",
                "[DELAY=5s][MESSAGE] &aYou had a message sent to you, with a delay of &f5 seconds&a.",
                "[CHANCE=30][DELAY=5s][MESSAGE] &aYou had a &f30% &achance of getting this message, with a &f3s &adelay!"
        )

        for (message in messages) {

        }
    }
}