package general

/**
 * Created by Daimhim on 2017/8/25.
 */
object ClassHelp {
    fun <T> create(cla: Class<*>): T? {
        try {
            return cla.newInstance() as T
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }

}