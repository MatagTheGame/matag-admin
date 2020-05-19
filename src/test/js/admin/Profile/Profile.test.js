import 'babel-polyfill'
import ApiClientStub from '../utils/ApiClientStub'
import TestUtils from '../utils/TestUtils'
import Browser from '../../Browser'

describe('Profile', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should display profile', async () => {
    // Given
    ApiClientStub.stubActiveGame()
    TestUtils.userIsLoggedIn()

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/profile'))
    await browser.waitUntilLoaded()

    // Then
    browser.expectTitleToBe('Profile')
    browser.getProfileSection('profile').validateProfile(TestUtils.DEFAULT_PROFILE)
  })
})
