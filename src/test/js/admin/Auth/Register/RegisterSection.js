import {fireEvent, getNodeText, screen, waitFor} from '@testing-library/react'

export default class RegisterSection {
  constructor(element) {
    this.element = element
  }

  setEmail(email) {
    fireEvent.change(screen.getByLabelText(/Email/), {target: {value: email}})
  }

  setUsername(username) {
    fireEvent.change(screen.getByLabelText(/Username/), {target: {value: username}})
  }

  setPassword(password) {
    fireEvent.change(screen.getByLabelText(/Password/), {target: {value: password}})
  }

  register() {
    fireEvent.click(this.element.querySelector('.register-buttons input'))
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