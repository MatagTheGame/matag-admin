import {getNodeText} from '@testing-library/react'

export default class IntoSection {
  constructor(element) {
    this.element = element
  }

  validateAllLinks() {
    const links = this.element.querySelectorAll('a')
    expect(getNodeText(links[0])).toBe('Matag: The Game')
    expect(getNodeText(links[1])).toBe('matag.the.game@gmail.com')
    expect(getNodeText(links[2])).toBe('Matag: The Game Discord Channel')
  }
}