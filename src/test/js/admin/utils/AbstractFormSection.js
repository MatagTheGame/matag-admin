import {getNodeText, waitFor} from '@testing-library/react'

export default class AbstractFormSection {
  constructor(element) {
    this.element = element
  }

  async waitForLoaderToDisappear() {
    await waitFor(() => !this.element.querySelector('.loader'))
  }

  getError() {
    return getNodeText(this.element.querySelector('.error'))
  }

  getMessage() {
    return getNodeText(this.element.querySelector('.message'))
  }
}