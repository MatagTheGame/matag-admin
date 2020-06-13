import {fireEvent, screen} from '@testing-library/react'
import AbstractFormSection from '../../utils/AbstractFormSection'

export default class LoginSection extends AbstractFormSection {
  setUsername(username) {
    fireEvent.change(screen.getByLabelText(/Email or Username/), {target: {value: username}})
  }

  setPassword(password) {
    fireEvent.change(screen.getByLabelText(/Password/), {target: {value: password}})
  }

  login() {
    fireEvent.click(this.element.querySelectorAll('.form-buttons input')[0])
  }
}