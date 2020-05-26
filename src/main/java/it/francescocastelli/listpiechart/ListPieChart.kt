package it.francescocastelli.listpiechart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class ListPieChart @JvmOverloads constructor(
    viewContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(viewContext, attrs, defStyleAttr){

    private var rectF:RectF = RectF(100f,100f,200f,200f)

    var data: MutableList<Entry> = mutableListOf()
    var pieColor:Int = Color.WHITE

    private fun executeProportion(value: Float,sumOfValues:Float):Float{
        return value*360/sumOfValues
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerWidth = (width/2).toFloat()
        val centerHeight = (height/2).toFloat()
        val totalRadiusCircle = 85f
        val arcDepth = 5f

        rectF.apply {
            left = centerWidth-totalRadiusCircle - arcDepth
            top = centerHeight-totalRadiusCircle - arcDepth
            right = centerWidth+totalRadiusCircle + arcDepth
            bottom = centerHeight+totalRadiusCircle + arcDepth
        }
        canvas?.apply {
            val sumOfValues = data.map { it.value }.sum()
            val listOfPaints = data.map { Paint(Paint.ANTI_ALIAS_FLAG).apply { color = it.color } }
            if (data.isNotEmpty()){
                for (i in data.indices){
                    if (i==0)
                        drawArc(rectF,0f,executeProportion(data[i].value,sumOfValues),true,listOfPaints[i]) // x:360 = entry:sumOfValues -> x=360*entry/sumOfAll
                    else{
                        val sumOfPreviousValues = data.subList(0,i).map { it.value }.sum()
                        //this command gets the values of the previous values to determine the startAngle

                        val startAngle = executeProportion(sumOfPreviousValues,sumOfValues)

                        val sweepAngle = executeProportion(data[i].value,sumOfValues)

                        drawArc(rectF,startAngle,sweepAngle,true,listOfPaints[i])
                    }
                }
            }

            drawCircle(centerWidth,centerHeight,totalRadiusCircle,whiteCornerPaint)
            primaryColorPaint.color = pieColor
            drawCircle(centerWidth,centerHeight,80f,primaryColorPaint)

        }
    }

    private val whiteCornerPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    private val primaryColorPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = pieColor
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}

data class Entry(
    var value:Float,
    var color:Int
)