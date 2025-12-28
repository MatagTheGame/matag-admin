import 'babel-polyfill'
import ApiClientStub from '../utils/ApiClientStub'
import TestUtils from '../utils/TestUtils'
import Browser from '../../Browser'

describe('Home', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should load homepage for non logged in user', async () => {
    // Given
    TestUtils.userIsNotLoggedIn()

    // When
    const browser = new Browser(TestUtils.renderAdminApp())
    await browser.waitUntilLoaded()

    // Then
    browser.getHeader().expectTitleToBeMatagTheGame()
    await browser.waitForTitleToBe('Home')
    browser.getHeader().expectMenuItems(['Home', 'Login', 'Register'])
    browser.getIntroSection().validateAllLinks()
    browser.getStatsSection().validateStats(TestUtils.DEFAULT_STATS)
    expect(browser.getLoginSection().element).toBeDefined()
    expect(browser.getPlaySection().element).toBeNull()
  })

  test('Should load homepage for logged in user', async () => {
    // Given
    ApiClientStub.stubActiveGame()
    TestUtils.loginUser()

    // When
    const browser = new Browser(TestUtils.renderAdminApp())
    await browser.waitUntilLoaded()

    // Then
    browser.getHeader().expectTitleToBeMatagTheGame()
    browser.getHeader().expectMenuItems(['Home', 'Decks', 'Play', 'User1'])
    await browser.waitForTitleToBe('Home')
    browser.getIntroSection().validateAllLinks()
    browser.getStatsSection().validateStats(TestUtils.DEFAULT_STATS)
    expect(browser.getPlaySection().element).toBeDefined()
    expect(browser.getLoginSection().element).toBeNull()
  })
})
