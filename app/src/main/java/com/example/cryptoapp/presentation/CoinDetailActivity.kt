package com.example.cryptoapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityCoinDetailBinding
import javax.inject.Inject
import kotlin.getValue

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var fromSymbol: String
    private val binding by lazy {
        ActivityCoinDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseArgs()
        if (savedInstanceState == null) {
            openFragment()
        }
    }

    private fun parseArgs() {
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            throw (RuntimeException("Intent has bad data"))
        }
        fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL) ?: EMPTY_SYMBOL
    }

    private fun openFragment() {
        val fragment = CoinDetailFragment.newInstance(fromSymbol)
        supportFragmentManager.beginTransaction()
            .replace(R.id.coin_detail_container, fragment)
            .commit()
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        private const val EMPTY_SYMBOL = ""

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}
