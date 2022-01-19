package ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myproevent.databinding.ItemPickedContactBinding
import ru.myproevent.domain.utils.load
import ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker.IPickedContactItemView

class PickedContactsRVAdapter(val presenter: IPickedContactsListPresenter) :
    RecyclerView.Adapter<PickedContactsRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemPickedContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    inner class ViewHolder(private val vb: ItemPickedContactBinding) :
        RecyclerView.ViewHolder(vb.root),
        IPickedContactItemView {

        override var pos = -1

        init {
            itemView.setOnClickListener {
                presenter.onItemClick(this)
            }
        }

        override fun setName(name: String) {
            vb.name.text = name
        }

        override fun loadImg(url: String) {
            vb.ivImg.load(url)
        }
    }
}