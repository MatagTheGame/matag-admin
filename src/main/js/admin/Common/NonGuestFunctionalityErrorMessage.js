import React, {Component} from 'react'
import './loader.scss'

export default class NonGuestFunctionalityErrorMessage extends Component {
  render() {
    return (
      <div>
        This functionality is available only to non Guest users.
      </div>
    )
  }
}
