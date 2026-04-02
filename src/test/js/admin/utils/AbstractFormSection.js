import {waitFor} from '@testing-library/react'

export default class AbstractFormSection {
  constructor(element) {
    this.element = element
  }

  async waitForLoaderToDisappear() {
    await waitFor(() => !this.element.querySelector('.loader'))
  }

  getError() {
    return this.element.querySelector('.error').textContent
  }

  getMessage() {
    return this.element.querySelector('.message').textContent
  }
}