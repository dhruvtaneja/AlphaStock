package com.dt.alphastock

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private var quoteFragment = QuoteFragment.newInstance()
    private var comparisonFragment = ComparisonFragment.newInstance()
    private var activeFragment: Fragment = quoteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(supportFragmentManager) {
            beginTransaction()
                .add(R.id.fragment_container, comparisonFragment, "compare")
                .hide(comparisonFragment)
                .commit()
            beginTransaction()
                .add(R.id.fragment_container, quoteFragment, "quote")
                .commit()
        }

        bottom_navigation_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_get_quote -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(quoteFragment).commit()
                    activeFragment = quoteFragment
                }
                R.id.action_compare -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(comparisonFragment).commit()
                    activeFragment = comparisonFragment
                }
            }
            true
        }
    }
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}
