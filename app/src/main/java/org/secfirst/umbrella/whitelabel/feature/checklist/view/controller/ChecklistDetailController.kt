package org.secfirst.umbrella.whitelabel.feature.checklist.view.controller

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.alert_control.view.*
import kotlinx.android.synthetic.main.checklist_add_item_dialog.view.*
import kotlinx.android.synthetic.main.checklist_detail_view.*
import kotlinx.android.synthetic.main.checklist_detail_view.view.*
import kotlinx.android.synthetic.main.form_progress.*
import org.jetbrains.anko.design.snackbar
import org.secfirst.umbrella.whitelabel.R
import org.secfirst.umbrella.whitelabel.UmbrellaApplication
import org.secfirst.umbrella.whitelabel.component.SwipeToDeleteCallback
import org.secfirst.umbrella.whitelabel.data.database.checklist.Checklist
import org.secfirst.umbrella.whitelabel.data.database.checklist.Content
import org.secfirst.umbrella.whitelabel.feature.base.view.BaseController
import org.secfirst.umbrella.whitelabel.feature.checklist.DaggerChecklistComponent
import org.secfirst.umbrella.whitelabel.feature.checklist.interactor.ChecklistBaseInteractor
import org.secfirst.umbrella.whitelabel.feature.checklist.presenter.ChecklistBasePresenter
import org.secfirst.umbrella.whitelabel.feature.checklist.view.ChecklistView
import org.secfirst.umbrella.whitelabel.feature.checklist.view.adapter.ChecklistAdapter
import org.secfirst.umbrella.whitelabel.feature.checklist.view.controller.ChecklistCustomController.Companion.EXTRA_ID_CUSTOM_CHECKLIST
import org.secfirst.umbrella.whitelabel.misc.initRecyclerView
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class ChecklistDetailController(bundle: Bundle) : BaseController(bundle), ChecklistView {

    @Inject
    internal lateinit var presenter: ChecklistBasePresenter<ChecklistView, ChecklistBaseInteractor>
    private lateinit var checklistView: View
    private lateinit var checklistDialogView: View
    private lateinit var addItemDialog: Dialog
    private val checklistItemClick: (Content) -> Unit = this::onChecklistItemClicked
    private val checklistProgress: (Int) -> Unit = this::onUpdateChecklistProgress
    private lateinit var checklist: Checklist
    private val checklistId by lazy { args.getString(EXTRA_ID_CUSTOM_CHECKLIST) }
    private lateinit var adapter: ChecklistAdapter

    constructor(checklistId: String) : this(Bundle().apply {
        putString(EXTRA_ID_CUSTOM_CHECKLIST, checklistId)
    })

    override fun onInject() {
        DaggerChecklistComponent.builder()
                .application(UmbrellaApplication.instance)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        disableNavigation()
        presenter.onAttach(this)
        presenter.submitChecklist(checklistId)
        checklistDialogView = inflater.inflate(R.layout.checklist_add_item_dialog,
                container, false)
        checklistView = inflater.inflate(R.layout.checklist_detail_view,
                container, false)
        createInsertItemDialog()

        checklistDialogView.alertControlOk.setOnClickListener { addNewContent() }
        checklistDialogView.alertControlCancel.setOnClickListener { addItemDialog.dismiss() }
        checklistView.addNewItemChecklist.setOnClickListener { addItemDialog.show() }

        setUpToolbar(checklistView)
        return checklistView
    }

    private fun initSwipeDelete() {
        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                onDeleteChecklist(adapter.getChecklistItem(position))
                adapter.removeAt(position)
                onUpdateChecklistProgress(Math.ceil(checklist.content.filter
                { it.value }.size * 100.0 / checklist.content.size).toInt())
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(checklistDetailRecyclerView)
    }

    private fun onUpdateChecklistProgress(percentage: Int) {
        if (percentage <= 0) {
            titleProgressAnswer.text = "0%"
            progressAnswer.progress = 0
        } else {
            titleProgressAnswer.text = "$percentage%"
            progressAnswer.progress = percentage
        }
        checklist.progress = progressAnswer.progress
        presenter.submitUpdateChecklist(checklist)
    }

    override fun getChecklist(checklist: Checklist) {
        this.checklist = checklist
        adapter = ChecklistAdapter(checklist.content, checklistItemClick, checklistProgress)
        checklistDetailRecyclerView?.initRecyclerView(adapter)
        initSwipeDelete()
        currentProgress()
        if (checklist.custom) addNewItemChecklist.visibility = View.VISIBLE
    }

    private fun createInsertItemDialog() {
        addItemDialog = AlertDialog.Builder(context)
                .setView(checklistDialogView)
                .create()
    }

    private fun addNewContent() {
        val newItem = checklistDialogView.editChecklistItem.text.toString()
        if (newItem.isNotBlank()) {
            adapter.add(Content(check = newItem, checklist = checklist))
            onChecklistUpdated(checklist)
            checklistDialogView.editChecklistItem.setText("")
            checklistView.snackbar(context.getString(R.string.checklist_add_item_message))
        } else {
            checklistDialogView.editChecklistItem.error =
                    context.getString(R.string.checklist_custom_empty_item_error_message)
        }
    }

    private fun setUpToolbar(view: View) {
        view.checklistDetailToolbar.let {
            mainActivity.setSupportActionBar(it)
            mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            mainActivity.supportActionBar?.title = getTitle()
        }
    }

    private fun onChecklistUpdated(checklist: Checklist) =
            presenter.submitUpdateChecklist(checklist)

    private fun onDeleteChecklist(checklistItem: Content) =
            presenter.submitDeleteChecklistContent(checklistItem)

    private fun onChecklistItemClicked(checklistItem: Content) =
            presenter.submitInsertChecklistContent(checklistItem)

    private fun currentProgress() {
        progressAnswer.progress = checklist.progress
        titleProgressAnswer.text = "${checklist.progress}%"
    }

    override fun onDestroy() {
        super.onDestroy()
        enableNavigation()
    }

    private fun getTitle() = context.getString(R.string.checklistDetail_title)
}