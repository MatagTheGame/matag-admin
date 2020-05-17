/* global describe, expect */

import {screen, waitFor, getNodeText} from '@testing-library/react'
import 'babel-polyfill'
import TestUtils from '../../TestUtils'

TestUtils.mockConfig()
TestUtils.mockStats()
TestUtils.mockActiveGame()
TestUtils.mockProfile()
TestUtils.mockAuthHelper()

test('Should load homepage', async () => {
  const app = TestUtils.renderAdminApp()
  await waitFor(() => app.container.querySelector('#home'))
  expect(getNodeText(app.container.querySelector('h1'))).toBe('Matag: The Game')
  throw Error('')
})
