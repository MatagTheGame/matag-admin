import '@testing-library/jest-dom'

const fetchMock = require('jest-fetch-mock')
fetchMock.enableMocks()

const { TextEncoder, TextDecoder } = require('util')
global.TextEncoder = TextEncoder
global.TextDecoder = TextDecoder
