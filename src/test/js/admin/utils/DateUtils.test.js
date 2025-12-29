import DateUtils from 'admin/utils/DateUtils'

describe('DateUtils', () => {
  test('parse / format', () => {
    // Given
    const string = '2020-04-18T08:00:00'

    // When
    const date = DateUtils.parse(string)
    const formatted = DateUtils.formatDateTime(date)

    // Then
    expect(formatted).toBe('18/04/2020, 08:00:00')
  })
})
