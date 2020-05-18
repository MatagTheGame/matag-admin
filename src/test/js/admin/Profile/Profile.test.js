import 'babel-polyfill'
import TestUtils from '../../TestUtils'
import Browser from '../../Browser'

describe('Profile', () => {
  beforeEach(() => {
    TestUtils.mockConfigAndStats()
  });

  afterEach(() => {
    TestUtils.resetMocks()
  });

  test('Should display profile', async () => {
    // Given
    TestUtils.mockActiveGame()
    TestUtils.userIsLoggedIn()

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/profile'))
    await browser.waitUntilLoaded()

    // Then
    browser.expectTitleToBe('Profile')
    browser.getProfileSection('profile').validateProfile(TestUtils.DEFAULT_PROFILE)
  })
})
