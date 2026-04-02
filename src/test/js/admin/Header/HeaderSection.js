export default class HeaderSection {
  constructor(element) {
    this.element = element
  }

  expectTitleToBeMatagTheGame() {
    expect(this.element.querySelector('h1').textContent).toBe('Matag: The Game')
  }

  expectMenuItems(expectedItemsText) {
    const items = this.element.querySelectorAll('nav')
    const itemsText = Array.from(items).map((item) => item.childNodes[0].textContent)
    expect(itemsText).toEqual(expectedItemsText)
  }
}