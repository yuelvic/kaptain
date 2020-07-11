package cafe.adriel.kaptain.sample.feature.b

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.kaptain.Kaptain
import cafe.adriel.kaptain.sample.feature.shared.Destination
import com.tombayley.activitycircularreveal.CircularReveal
import kotlinx.android.synthetic.main.activity_feature_b.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class FeatureBActivity : AppCompatActivity(), KoinComponent {

    private val kaptain by inject<Kaptain>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_b)

        kaptain.fromIntent<Destination.FeatureB>(this)
            ?.message
            ?.let(vMessage::setText)

        CircularReveal(window.decorView.rootView).apply {
            onActivityCreate(intent)
        }
    }
}