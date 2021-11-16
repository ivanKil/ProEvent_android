package ru.myproevent.ui.adapters

interface IListPresenter<V> {
    fun onItemClick(view: V)
    fun bindView(view: V)
    fun getCount(): Int
}