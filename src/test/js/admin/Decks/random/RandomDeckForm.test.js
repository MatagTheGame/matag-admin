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
    expect(browser.getPlayFormSection().inputOfColor('White')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('Blue')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('Black')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('Red')).not.toBeChecked()
    expect(browser.getPlayFormSection().inputOfColor('Green')).not.toBeChecked()

    // Selecting some colors
    browser.getPlayFormSection().clickInputOfColor('White')
    expect(browser.getPlayFormSection().inputOfColor('White')).toBeChecked()
    browser.getPlayFormSection().clickInputOfColor('Blue')
    expect(browser.getPlayFormSection().inputOfColor('Blue')).toBeChecked()

    // Deselecting a color
    browser.getPlayFormSection().clickInputOfColor('White')
    expect(browser.getPlayFormSection().inputOfColor('White')).not.toBeChecked()
  })
})
