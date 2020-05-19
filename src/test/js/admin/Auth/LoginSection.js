import {fireEvent, screen} from '@testing-library/react'

export default class LoginSection {
  constructor(element) {
    this.element = element
  }

  setUsername(username) {
    fireEvent.change(screen.getByLabelText(/Email or Username/), {target: {value: username}})
  }

  setPassword(password) {
    fireEvent.change(screen.getByLabelText(/Password/), {target: {value: password}})
  }

  login() {
    fireEvent.click(this.element.querySelectorAll('.login-buttons input')[0])
  }
}