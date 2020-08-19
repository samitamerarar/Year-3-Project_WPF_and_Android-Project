package com.example.light_client.ui.game

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.*
import com.example.light_client.lib.CanvasView
import java.lang.Math.max


class ProgressiveDraw(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var bitmap: Bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    var counter = false
    var segmentList: MutableList<MutableList<Path>> = arrayListOf(ArrayList())
    var idSegmentToPositionMap = mutableMapOf<Int, Int>()
    private var idSegmentToStyleMap = mutableMapOf<Int, Triple<Int, Float, Paint.Cap>>()

    /* Tutorial Stuff */
    private var pathForTutorial: Path = Path()
    private var paintForTutorial: Paint = Paint()
    private var lengthForTutorial: Float = 0.toFloat()
    private var tutorialView = false

    fun initTutorialView() {
        tutorialView = true
        paintForTutorial = Paint()
        paintForTutorial.color = Color.BLACK
        paintForTutorial.strokeWidth = 15f
        paintForTutorial.style = Paint.Style.STROKE
        paintForTutorial.isAntiAlias = true
        pathForTutorial = Path()
        pathForTutorial.moveTo(50f, 250f)
        pathForTutorial.lineTo(50f, 500f)
        pathForTutorial.lineTo(150f, 500f)
        pathForTutorial.lineTo(150f, 300f)
        pathForTutorial.lineTo(500f, 300f)
        pathForTutorial.lineTo(500f, 500f)
        pathForTutorial.lineTo(600f, 500f)
        pathForTutorial.lineTo(600f, 250f)
        pathForTutorial.lineTo(48f, 250f)

        // Calculate path length
        lengthForTutorial = PathMeasure(pathForTutorial, true).length
        // Call animator to draw path with duration
        val animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f)
        animator.duration = 10000 // in ms
        animator.start()
    }
    // Animator for Tutorial
    @Suppress("unused")
    fun setPhase(phase: Float) {
        paintForTutorial.pathEffect = drawGradually(lengthForTutorial, phase, 0.0f)
        invalidate()
    }
    // for Tutorial
    private fun drawGradually(pathLength: Float, phase: Float, offset: Float): PathEffect {
        return DashPathEffect(
            floatArrayOf(pathLength, pathLength),
            kotlin.math.max(phase * pathLength, offset)
        )
    }

    /* // */
    private var paint: Paint = Paint()
    var svgPath = Path()
    var isSVG = false

    init {
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
    }

    fun clearCanvas() {
        this.segmentList.clear()
        this.idSegmentToPositionMap = mutableMapOf()
        this.idSegmentToStyleMap = mutableMapOf()
        this.invalidate()
    }

    private fun setSegmentStyle(color: Int, strokeWidth: Float, cap: Paint.Cap) {
        paint.color = color
        paint.strokeWidth = strokeWidth
        paint.strokeCap = cap
    }

    fun addStyleToSegment(idSegment: Int, style: Triple<Int, Float, Paint.Cap>) {
        if (idSegmentToPositionMap.containsKey(idSegment)) idSegmentToStyleMap[idSegment] = style
    }

    fun addPointToList(idSegment: Int, point: CanvasView.Point) {
        val pair: Pair<Float, Float> = Pair(point.x, point.y)
        if (!idSegmentToPositionMap.containsKey(idSegment)) {
            val segment: MutableList<Path> = ArrayList()
            addPath(pair.first, pair.second, pair.first, pair.second, segment)
            segmentList.add(segment)
            idSegmentToPositionMap[idSegment] = segmentList.size-1
        } else {
            segmentList[idSegmentToPositionMap[idSegment]!!].last().lineTo(pair.first, pair.second)
        }
    }

    private fun addPath(startx: Float, starty: Float, endx: Float, endy: Float, segment: MutableList<Path>): MutableList<Path> {
        val path = Path()
        path.moveTo(startx, starty)
        path.lineTo(endx, endy)
        segment.add(path)

        return segment
    }

    private fun getKeyFromValueInHashMap(hashmap: Map<Int, Int>, value: Int): Int {
        for (o in hashmap.keys) if (hashmap[o] == value) return o
        return 0
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!tutorialView) {
            if (!isSVG) {
                if(segmentList.size > 0 && emptyList<Path>() !in segmentList) {
                    for (segment in segmentList) {
                        val segId = getKeyFromValueInHashMap(
                            idSegmentToPositionMap,
                            segmentList.indexOf(segment)
                        )
                        setSegmentStyle(
                            idSegmentToStyleMap[segId]!!.first,
                            idSegmentToStyleMap[segId]!!.second, idSegmentToStyleMap[segId]!!.third
                        )
                        for (path in segment) {
                            canvas.drawPath(path, paint)
                        }
                    }
                }
            }
            else {
                val svgPaint = Paint()
                svgPaint.style = Paint.Style.STROKE
                svgPaint.isAntiAlias = true
                svgPaint.color = Color.BLACK
                svgPaint.strokeJoin = Paint.Join.ROUND
                svgPaint.strokeCap = Paint.Cap.ROUND
                svgPaint.strokeWidth = 10f
                canvas.drawPath(this.svgPath, svgPaint)
            }
            // fix the issue where after switching from Paint -> Passive the drawn image is not kept
            if (counter && !(bitmap.sameAs(Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)))) drawBitmap(canvas)
        }
        else {
            canvas.drawPath(pathForTutorial, paintForTutorial)
        }
    }

    // fix the issue where after switching from Paint -> Passive the drawn image is not kept
    private fun drawBitmap(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawBitmap(this.bitmap, x, y, paint)
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    }
}
