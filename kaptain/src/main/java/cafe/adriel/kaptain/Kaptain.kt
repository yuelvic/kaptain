package cafe.adriel.kaptain

import android.app.Activity
import android.content.Intent
import android.view.View
import com.tombayley.activitycircularreveal.CircularReveal
import kotlin.reflect.KClass

class Kaptain(init: Kaptain.() -> Unit) {

    @PublishedApi
    internal companion object {

        const val EXTRA_DESTINATION = "kaptain.destination"
    }

    @PublishedApi
    internal val destinations = mutableMapOf<KClass<out KaptainDestination>, KClass<out Activity>>()

    init {
        this.init()
    }

    @Throws(KaptainException::class)
    fun navigate(activity: Activity, destination: KaptainDestination, requestCode: Int? = null,
                 flags: ArrayList<Int> = arrayListOf()) {
        val destinationClass = destinations[destination::class]?.java
            ?: throw KaptainException("Destination not found -> ${destination::class.qualifiedName}")

        val destinationIntent = Intent(activity, destinationClass)
            .putExtra(EXTRA_DESTINATION, destination)

        flags.iterator().forEach { destinationIntent.addFlags(it) }

        requestCode
            ?.let { activity.startActivityForResult(destinationIntent, requestCode) }
            ?: activity.startActivity(destinationIntent)
    }

    @Throws(KaptainException::class)
    fun navigateWithCircularReveal(activity: Activity, originView: View, duration: Long = 1000,
                                   destination: KaptainDestination, requestCode: Int? = null) {
        val destinationClass = destinations[destination::class]?.java
            ?: throw KaptainException("Destination not found -> ${destination::class.qualifiedName}")

        val destinationIntent = Intent(activity, destinationClass)
            .putExtra(EXTRA_DESTINATION, destination)

        CircularReveal.presentActivity(CircularReveal.Builder(activity, originView, destinationIntent, duration).apply {
            requestCode?.let { this.requestCode = it }
        })
    }

    inline fun <reified D : KaptainDestination> fromIntent(activity: Activity): D? =
        activity.intent.getSerializableExtra(EXTRA_DESTINATION) as? D

    inline fun <reified D : KaptainDestination, reified A : Activity> add() {
        destinations += D::class to A::class
    }

    inline fun <reified D : KaptainDestination> remove() {
        destinations -= D::class
    }

    inline fun <reified D : KaptainDestination> has(): Boolean =
        destinations.containsKey(D::class)
}