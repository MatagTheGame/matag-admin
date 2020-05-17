/* global test, expect */

import {renderAdminApp, mockConfig, mockProfile} from "../../test-utils"
import {screen, waitFor} from '@testing-library/react'
import 'babel-polyfill'

mockConfig()
mockProfile()

test('Should load homepage', async () => {
  const app = renderAdminApp()
  const home = await waitFor(() => app.container.querySelector('#home'))
  screen.debug()
  console.log(home)
})
