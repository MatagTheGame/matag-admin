import {fireEvent} from '@testing-library/react'

export default class PlayFormSection {
  constructor(element) {
    this.element = element
  }

  inputOfColor(color) {
    return this.element.querySelectorAll(`#color-${color}`)[0]
  }

  clickInputOfColor(color) {
    fireEvent.click(this.inputOfColor(color))
  }
}