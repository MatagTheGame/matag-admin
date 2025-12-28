import 'babel-polyfill'
import ApiClientStub from '../../utils/ApiClientStub'
import TestUtils from '../../utils/TestUtils'
import Browser from '../../../Browser'

describe('Login', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
    TestUtils.loginUser()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should change password', async () => {
    // Given
    ApiClientStub.stubChangePassword({message: 'Password changed'})

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/change-password'))
    await browser.waitUntilLoaded()

    // And
    await browser.waitForTitleToBe('Change Password')
    browser.getChangePasswordSection().setOldPassword('old-password')
    browser.getChangePasswordSection().setNewPassword('new-password')
    browser.getChangePasswordSection().changePassword()

    // Then
    await browser.getChangePasswordSection().waitForLoaderToDisappear()
    expect(browser.getChangePasswordSection().getMessage()).toBe('Password changed')
  })

  test('Should fail changing password', async () => {
    // Given
    ApiClientStub.stubChangePassword({error: 'Password not matched'})

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/change-password'))
    await browser.waitUntilLoaded()

    // And
    await browser.waitForTitleToBe('Change Password')
    browser.getChangePasswordSection().setOldPassword('old-password')
    browser.getChangePasswordSection().setNewPassword('new-password')
    browser.getChangePasswordSection().changePassword()

    // Then
    await browser.getChangePasswordSection().waitForLoaderToDisappear()
    expect(browser.getChangePasswordSection().getError()).toBe('Password not matched')
  })
})
