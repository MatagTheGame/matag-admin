import 'babel-polyfill'
import ApiClientStub from '../utils/ApiClientStub'
import TestUtils from '../utils/TestUtils'
import Browser from '../../Browser'

describe('Login', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should login a user', async () => {
    // Given
    TestUtils.userIsNotLoggedIn()
    //ApiClientStub.loginReturns('token', TestUtils.DEFAULT_PROFILE)

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/login'))
    await browser.waitUntilLoaded()

    // Then
    browser.expectTitleToBe('Login')
    browser.getLoginSection().setUsername('MyUsername')
    browser.getLoginSection().setPassword('MyPassword')
    //browser.getLoginSection().login()
  })
})
