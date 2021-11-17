package ru.myproevent.ui.presenters

interface IListPresenter<V> {
    fun onItemClick(view: V)
    fun bindView(view: V)
    fun getCount(): Int
}