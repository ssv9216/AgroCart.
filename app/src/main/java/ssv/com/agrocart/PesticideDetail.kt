package ssv.com.agrocart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_pesticide_detail.*

class PesticideDetail : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesticide_detail)

        //import data from parent activity
        val title: String= intent.getStringExtra("title")
        val desc: String = intent.getStringExtra("desc")
        val price: String = intent.getStringExtra("price")
        val rating :String=intent.getStringExtra("rating")
        val image :Int=intent.getIntExtra("Image",0)

        //set data to fields
        apdTitle.text= title;
        apdDesc.text =desc;
        apdPrice.text = price;
        apdRating.text = rating;
        apdIv.setImageResource(image)

        var mToolBar = findViewById<Toolbar>(R.id.apdToolbar)


        setSupportActionBar(mToolBar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)


    }
}

