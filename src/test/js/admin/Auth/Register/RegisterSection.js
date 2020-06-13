import {fireEvent, screen} from '@testing-library/react'
import AbstractFormSection from '../../utils/AbstractFormSection'

export default class RegisterSection extends AbstractFormSection {
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
    fireEvent.click(this.element.querySelector('.form-buttons input'))
  }
}