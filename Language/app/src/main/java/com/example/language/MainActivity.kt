package com.example.language

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.language.adapter.ActionAdapter
import com.example.language.adapter.CategoryAdapter
import com.example.language.custom.*
import com.example.language.databinding.*
import com.example.language.model.Action
import com.example.language.model.Category
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.example.language.script.Run
import com.jmedeisis.draglinearlayout.DragLinearLayout

class MainActivity : AppCompatActivity() {

    private var viewList: MutableList<View> = ArrayList()

    private var deleteBtn: ImageButton? = null
    private var startBtn: ImageButton? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryList: MutableList<Category> = ArrayList()
        categoryList.add(Category(0, "Все"))
        categoryList.add(Category(1, "Переменные"))
        categoryList.add(Category(2, "Вывод"))
        categoryList.add(Category(3, "Условия"))
        categoryList.add(Category(4, "Цикл"))
        setCategoryRecycler(categoryList)

        actionList.add(Action(1, "Создание\nпеременной", 1))
        actionList.add(Action(2, "Вывод в\nконсоль", 2))
        actionList.add(Action(3, "Если ...", 3))
        actionList.add(Action(4, "Пока ...", 4))
        actionList.add(Action(5, "Операции над\nпеременными", 1))
        fullActionList.addAll(actionList)
        setActionRecycler(actionList)

        val sheet = findViewById<FrameLayout>(R.id.sheet)
        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 90
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val addText = sheet.findViewById<TextView>(R.id.print)
        val container = findViewById<DragLinearLayout>(R.id.addField)

        deleteBtn = findViewById(R.id.delete)
        deleteBtn?.setOnClickListener {
            viewList.clear()
            container.removeAllViews()
            addText.text = ""
        }

        startBtn = findViewById(R.id.start)
        startBtn?.setOnClickListener {
            val readyData: String = createString(container, "")
            if (readyData != "Error") {
                val run = Run(readyData)
                addText.text = run.go()
            }
        }

        val scroll = findViewById<ScrollView>(R.id.scrollView)
        container.setContainerScrollView(scroll)

    }

    private fun typeOfError(string: String) {
        when (string) {
            "Error void Data" -> Toast.makeText(
                applicationContext, "Пустое значение переменной",
                Toast.LENGTH_LONG
            ).show()
            "Error in Name" -> Toast.makeText(
                applicationContext, "Переменная названа\nнекорректно",
                Toast.LENGTH_LONG
            ).show()
            "Error void Name" -> Toast.makeText(
                applicationContext, "Пустое название переменной",
                Toast.LENGTH_LONG
            ).show()
            "Error in Data" -> Toast.makeText(
                applicationContext, "Значение переменной введено\nнекорректно",
                Toast.LENGTH_LONG
            ).show()
            "Error void Condition" -> Toast.makeText(
                applicationContext, "Пустое условие",
                Toast.LENGTH_LONG
            ).show()
            "Error void elem" -> Toast.makeText(
                applicationContext, "Пустое поле для вывода",
                Toast.LENGTH_LONG
            ).show()
            "Error Else" -> Toast.makeText(
                applicationContext, "Слишком много иначе!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createString(layout: DragLinearLayout, string: String, key: Int = 0): String {
        val len: Int = layout.childCount

        var newString: String = string
        var i = 0
        var counterElse = 0
        while (i < len) {
            val view: View = layout.getChildAt(i)
            var addString = when (view) {
                is CustomCreateVarsView -> view.readyString()
                is CustomIfView -> view.readyString()
                is CustomElseView -> view.readyString()
                is CustomOperationsOnValView -> view.readyString()
                is CustomOutputView -> view.readyString()
                is CustomWhileView -> view.readyString()
                else -> "Error"
            }
            if (view is CustomElseView) {
                counterElse++
                if (counterElse > 1) {
                    return "Error Else"
                }
            }
            if (!(addString.contains(Regex("Error")))) {
                if ((view is CustomIfView) || (view is CustomWhileView)) {
                    addString = createString(view.findViewById(R.id.addInIfField), addString, 1)

                    if ((addString.contains(Regex("Error"))) && addString != "Error") {
                        typeOfError(addString)
                        return "Error"
                    }
                }
                newString += addString
                i++
                if ((i == len) && (key == 1)) {
                    newString += "END\n"
                }
            } else {
                typeOfError(addString)
                return "Error"
            }
        }

        return if (newString.contains(Regex("Error"))) {
            "Error"
        } else {
            newString
        }
    }

    private fun showPopupMenu(v: View, parent: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, v)
        val container = parent.findViewById<DragLinearLayout>(R.id.addInIfField)
        popupMenu.inflate(R.menu.popupmenu)
        popupMenu
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.create -> {
                        addCreateVarsView(container)
                        true
                    }
                    R.id.operation -> {
                        addOperationsOnVal(container)
                        true
                    }
                    R.id.output -> {
                        addOutputView(container)
                        true
                    }
                    R.id.ifBlock -> {
                        addIfView(container)
                        true
                    }
                    R.id.elseBlock -> {
                        addElseView(container)
                        true
                    }

                    R.id.whileBlock -> {
                        addWhileView(container)
                        true
                    }
                    else -> false
                }
            }
        popupMenu.setOnDismissListener {
            Toast.makeText(
                applicationContext, "onDismiss",
                Toast.LENGTH_SHORT
            ).show()
        }
        popupMenu.show()
    }

    private fun setActionRecycler(courseList: List<Action>) {
        val courses: List<Action> = courseList
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val actionRecycler: RecyclerView = findViewById(R.id.courseRecycler)
        actionRecycler.layoutManager = layoutManager
        actionAdapter = ActionAdapter { position -> onListItemClick(position) }
        actionRecycler.adapter = actionAdapter
        actionAdapter!!.setAllActions(courses)
    }

    private fun setCategoryRecycler(categoryList: List<Category>) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val categoryRecycler: RecyclerView = findViewById(R.id.categoryRecycler)
        categoryRecycler.layoutManager = layoutManager
        val categoryAdapter = CategoryAdapter(this, categoryList)
        categoryRecycler.adapter = categoryAdapter
    }

    private fun onListItemClick(position: Int) {

        when (actionList[position].id) {
            1 -> addCreateVarsView()
            2 -> addOutputView()
            3 -> addIfView()
            4 -> addWhileView()
            5 -> addOperationsOnVal()
        }
    }

    private fun addInIfView(view: View) {
        val addBtn: ImageButton = view.findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            showPopupMenu(addBtn, view)
        }
    }

    private fun addCreateVarsView(
        container: DragLinearLayout = findViewById(R.id.addField)
    ) {
        val view = CustomCreateVarsView(this)

        container.addView(view)
        container.setViewDraggable(view, view)

        val binding = CustomCreateVarsBinding.bind(view)


        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varName(binding.editName.text.toString())
            }

        })

        binding.editData.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varData(binding.editData.text.toString())
            }

        })

    }

    private fun addOutputView(
        container: DragLinearLayout = findViewById(R.id.addField)
    ) {
        val view = CustomOutputView(this)

        container.addView(view)

        val binding = CustomOutputBinding.bind(view)
        container.setViewDraggable(view, view)


        binding.outputElems.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varVariables(binding.outputElems.text.toString())
            }

        })
    }

    private fun addIfView(
        container: DragLinearLayout = findViewById(R.id.addField)
    ) {
        val view = CustomIfView(this)

        container.addView(view)
        container.setViewDraggable(view, view)

        val binding = CustomIfBinding.bind(view)

        addInIfView(view)


        binding.editCondition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varCondition(binding.editCondition.text.toString())
            }

        })
    }

    private fun addElseView(
        container: DragLinearLayout = findViewById(R.id.addField)
    ) {
        val view = CustomElseView(this)


        container.addView(view)
        container.setViewDraggable(view, view)

    }

    private fun addOperationsOnVal(
        container: DragLinearLayout = findViewById(R.id.addField)
    ) {
        val view = CustomOperationsOnValView(this)

        container.addView(view)
        container.setViewDraggable(view, view)

        val binding = CustomOperationsBinding.bind(view)


        binding.operationsInputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varName(binding.operationsInputName.text.toString())
            }

        })

        binding.operationsInputData.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varData(binding.operationsInputData.text.toString())
            }

        })
    }

    private fun addWhileView(
        container: DragLinearLayout = findViewById(R.id.addField)
    ) {
        val view = CustomWhileView(this)

        container.addView(view)
        container.setViewDraggable(view, view)

        val binding = CustomWhileBinding.bind(view)

        addInIfView(view)


        binding.editCondition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                view.varCondition(binding.editCondition.text.toString())
            }

        })
    }


    companion object {
        var actionAdapter: ActionAdapter? = null
        var actionList: MutableList<Action> = ArrayList()
        var fullActionList: MutableList<Action> = ArrayList()

        @SuppressLint("NotifyDataSetChanged")
        fun showCoursesByCategory(category: Int) {
            actionList.clear()
            actionList.addAll(fullActionList)
            val filterCourses: MutableList<Action> = ArrayList()
            for (c in actionList) {
                if (category != 0) {
                    if (c.category == category) filterCourses.add(c)
                } else {
                    filterCourses.add(c)
                }
            }
            actionList.clear()
            actionList.addAll(filterCourses)
            actionAdapter!!.notifyDataSetChanged()
        }
    }
}