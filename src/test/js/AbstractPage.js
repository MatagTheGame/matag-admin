import {waitFor} from '@testing-library/react'
import HeaderSection from './admin/Header/HeaderSection'

export default class AbstractPage {
  constructor(app) {
    this.app = app
  }

  getHeader() {
    return new HeaderSection(this.app.container.querySelector('header'))
  }

  async waitUntilLoaded() {
    await waitFor(() => this.app.container.querySelector('.page'))
  }
}