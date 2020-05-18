import {getNodeText} from '@testing-library/react'

export default class ProfileSection {
  constructor(element) {
    this.element = element
  }

  validateProfile(profile) {
    const dts = this.element.querySelectorAll('dt')
    const dds = this.element.querySelectorAll('dd')
    this.validateField(dts[0], dds[0], 'Username: ', profile.username)
    this.validateField(dts[1], dds[1], 'Type: ', profile.type)
  }

  validateField(dt, dd, expectedLabel, expectedValue) {
    expect(getNodeText(dt)).toBe(expectedLabel)
    expect(getNodeText(dd)).toBe(expectedValue)
  }
}