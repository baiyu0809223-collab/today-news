import { defineStore } from 'pinia'

export const useTabsStore = defineStore('tabs', {
  state: () => ({
    tabs: JSON.parse(localStorage.getItem('TABS')) || []
  }),

  actions: {
    addTab(route) {
      if (this.tabs.some(t => t.path === route.path)) return
      this.tabs.push({
        title: route.meta.title,
        path: route.path,
        affix: route.meta.affix || false
      })
      this.persist()
    },

    removeTab(path) {
      this.tabs = this.tabs.filter(t => t.path !== path)
      this.persist()
    },

    persist() {
      localStorage.setItem('TABS', JSON.stringify(this.tabs))
    }
  }
})