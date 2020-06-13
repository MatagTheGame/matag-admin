import React, {Component} from 'react'
import Loader from 'admin/Common/Loader'

export default class AbstractForm extends Component {
  constructor(props) {
    super(props)
  }

  displayError() {
    if (this.props.error) {
      return (
        <div className='grid grid-label-value'>
          <div/>
          <div className='error'>{this.props.error}</div>
        </div>
      )
    }
  }

  displayMessage() {
    if (this.props.message) {
      return (
        <div className='grid grid-label-value'>
          <div/>
          <div className='message'>{this.props.message}</div>
        </div>
      )
    }
  }

  displayLoader() {
    if (this.props.loading) {
      return <Loader center/>
    }
  }
}