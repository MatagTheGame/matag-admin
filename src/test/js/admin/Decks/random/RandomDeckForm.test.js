import ApiClientStub from '../../utils/ApiClientStub'
import TestUtils from '../../utils/TestUtils'
import Browser from '../../../Browser'

describe('RandomDeckForm', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should select different colors decks', async () => {
    // Given
    TestUtils.loginUser()

    // When
    const browser = new Browser(TestUtils.renderAdminApp())
    await browser.waitUntilLoaded()

    // Then
    expect(browser.getPlayFormSection().inputOfColor('white')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('blue')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('black')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('red')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('green')).not.toBeChecked()

    // Selecting some colors
    browser.getPlayFormSection().clickInputOfColor('white')
    expect(browser.getPlayFormSection().inputOfColor('white')).toBeChecked()
    browser.getPlayFormSection().clickInputOfColor('blue')
    expect(browser.getPlayFormSection().inputOfColor('blue')).toBeChecked()

    // Deselecting a color
    browser.getPlayFormSection().clickInputOfColor('white')
    expect(browser.getPlayFormSection().inputOfColor('white')).not.toBeChecked()
  })
})
