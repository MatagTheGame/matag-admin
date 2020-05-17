import 'babel-polyfill'
import TestUtils from '../../TestUtils'
import Browser from '../../Browser'

describe('Home', () => {
  beforeEach(() => {
    TestUtils.mockConfigAndStats()
  });

  afterEach(() => {
    TestUtils.resetMocks()
  });

  test('Should load homepage for non logged in user', async () => {
    // Given
    TestUtils.guestUser()

    // When
    const browser = new Browser(TestUtils.renderAdminApp())
    await browser.waitUntilLoaded()

    // Then
    browser.getHeader().expectTitleToBeMatagTheGame()
    browser.expectTitleToBeHome()
    browser.getHeader().expectMenuItems(['Home', 'Login', 'Register'])
    browser.getIntroSection().validateAllLinks()
    browser.getStatsSection().validateStats(TestUtils.DEFAULT_STATS)
    expect(browser.getLoginSection().element).toBeDefined()
    expect(browser.getPlaySection().element).toBeNull()
  })

  test('Should load homepage for logged in user', async () => {
    // Given
    TestUtils.mockActiveGame()
    TestUtils.userIsLoggedIn()

    // When
    const browser = new Browser(TestUtils.renderAdminApp())
    await browser.waitUntilLoaded()

    // Then
    browser.getHeader().expectTitleToBeMatagTheGame()
    browser.getHeader().expectMenuItems(['Home', 'Decks', 'Play', 'User1'])
    browser.expectTitleToBeHome()
    browser.getIntroSection().validateAllLinks()
    browser.getStatsSection().validateStats(TestUtils.DEFAULT_STATS)
    expect(browser.getPlaySection().element).toBeDefined()
    expect(browser.getLoginSection().element).toBeNull()
  })
})
