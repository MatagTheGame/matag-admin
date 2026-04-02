export default class IntoSection {
  constructor(element) {
    this.element = element
  }

  validateAllLinks() {
    const links = this.element.querySelectorAll('a')
    expect(links[0].textContent).toBe('Matag: The Game')
    expect(links[1].textContent).toBe('matag.the.game@gmail.com')
    expect(links[2].textContent).toBe('Matag: The Game Discord Channel')
  }
}