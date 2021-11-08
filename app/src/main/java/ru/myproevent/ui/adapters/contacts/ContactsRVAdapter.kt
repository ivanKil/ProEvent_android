package ru.myproevent.ui.adapters.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.ItemContactBinding
import ru.myproevent.domain.model.entities.Status
import ru.myproevent.utils.load


class ContactsRVAdapter(val presenter: IContactsListPresenter) :
    RecyclerView.Adapter<ContactsRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    override fun onViewRecycled(holder: ViewHolder) = holder.unbind()

    inner class ViewHolder(private val vb: ItemContactBinding) : RecyclerView.ViewHolder(vb.root),
        IContactItemView {

        init {
            itemView.setOnClickListener { presenter.onItemClick(this) }
        }

        override fun setName(name: String) {
            vb.tvName.text = name
        }

        override fun setDescription(description: String) {
            vb.tvDescription.text = description
        }

        override fun loadImg(url: String) {
            vb.ivImg.load(url)
        }

        override fun setStatus(status: Status) = with(vb) {
            when (status) {
                Status.REQUESTED ->
                    requestStatus.setImageDrawable(ProEventApp.instance.getDrawable(R.drawable.ic_incomming_request))
                Status.DECLINED ->
                    requestStatus.setImageDrawable(ProEventApp.instance.getDrawable(R.drawable.ic_rejected_request))
                Status.PENDING ->
                    requestStatus.setImageDrawable(ProEventApp.instance.getDrawable(R.drawable.ic_outgoing_request))
            }
        }

        override var pos = -1

        fun unbind() = with(vb) {
            // TODO: не понял зачем это
//            ivImg.setImageDrawable(null)
//            requestStatus.setImageDrawable(null)
        }
    }
}