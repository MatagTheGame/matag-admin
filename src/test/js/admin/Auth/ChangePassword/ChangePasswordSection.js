import {fireEvent, screen} from '@testing-library/react'
import AbstractFormSection from '../../utils/AbstractFormSection'

export default class ChangePasswordSection extends AbstractFormSection {
  setOldPassword(oldPassword) {
    fireEvent.change(screen.getByLabelText(/Old password/), {target: {value: oldPassword}})
  }

  setNewPassword(newPassword) {
    fireEvent.change(screen.getByLabelText(/New password/), {target: {value: newPassword}})
  }

  changePassword() {
    fireEvent.click(this.element.querySelectorAll('.form-buttons input')[0])
  }
}