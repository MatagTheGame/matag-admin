/* global describe, expect */

import TestUtils from '../../TestUtils'
import {createWaitForElement} from 'enzyme-wait'

TestUtils.mockConfig()
TestUtils.mockStats()
TestUtils.mockProfile()
TestUtils.mockActiveGame()
TestUtils.mockAuthHelper()

const delay = ms => new Promise(res => setTimeout(res, ms));

test('Should load homepage', async () => {
  console.log('Test started at: ', new Date())
  let app = TestUtils.mountAdminApp()
  console.log(app.html())
  delay(2000)
  console.log('Inspecting at: ', new Date())
  app.update()
  console.log(app.html())
})
