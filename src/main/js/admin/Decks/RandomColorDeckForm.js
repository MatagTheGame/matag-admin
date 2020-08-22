import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import ApiClient from 'admin/utils/ApiClient'

class RandomColorDeckForm extends Component {
  constructor(props) {
    super(props)
    this.handlePlay = this.handlePlay.bind(this)
    this.toggle = this.toggle.bind(this)
    this.state = {colors: []}
  }

  handlePlay(event) {
    event.preventDefault()
    this.setState({error: null})

    if (this.state.colors.length === 0) {
      this.setState({error: 'You need to select at least one color.'})
      return
    }

    const playerOptions = {
      randomColors: this.state.colors
    }

    const request = {
      gameType: 'UNLIMITED',
      playerOptions: JSON.stringify(playerOptions)
    }

    this.setState({loading: true})
    ApiClient.post('/game', request)
      .then(r => {
        this.setState({loading: false})
        if (r.gameId > 0) {
          this.setState({goToGameLoading: true})
          this.props.goToGame(r.gameId)

        } else {
          if (r.error) {
            this.setState({error: r.error})
          }
          if (r.activeGameId) {
            this.setState({activeGameId: r.activeGameId})
          }
        }
      })
  }

  isSelected(color) {
    return this.state.colors.indexOf(color) > -1
  }

  toggle(color) {
    const colors = this.state.colors
    if (this.isSelected(color)) {
      colors.splice(colors.indexOf(color), 1)
      this.setState({colors: colors})

    } else {
      this.setState({colors: [...colors, color]})
    }
  }

  displayGoToGame() {
    if (this.state.activeGameId) {
      return <span>Go to <a href='#' onClick={() => this.props.goToGame(this.state.activeGameId)}>game #{this.state.activeGameId}</a></span>
    }
  }

  displayError() {
    if (this.state.error) {
      return (
        <p className='message'>
          <span className='error'>{this.state.error}</span>
          {this.displayGoToGame()}
        </p>
      )
    }
  }

  displayLoader() {
    if (this.state.loading || this.state.goToGameLoading) {
      return <Loader center/>
    }
  }

  render() {
    return (
      <form id='play-form' className='matag-form' onSubmit={this.handlePlay}>
        <input type='hidden' name='session' value={AuthHelper.getToken()} />
        <p>Choose which colors you want to play:</p>
        <ul>
          <li>
            <input type='checkbox' id='color-white' name='white' checked={this.isSelected('WHITE')} onChange={() => this.toggle('WHITE')}/>
            <label htmlFor='color-white'><img src='/img/symbols/WHITE.png' alt='white'/>White</label>
          </li>
          <li>
            <input type='checkbox' id='color-blue' name='blue' checked={this.isSelected('BLUE')} onChange={() => this.toggle('BLUE')}/>
            <label htmlFor='color-blue'><img src='/img/symbols/BLUE.png' alt='blue'/>Blue</label>
          </li>
          <li>
            <input type='checkbox' id='color-black' name='black' checked={this.isSelected('BLACK')} onChange={() => this.toggle('BLACK')}/>
            <label htmlFor='color-black'><img src='/img/symbols/BLACK.png' alt='black'/>Black</label>
          </li>
          <li>
            <input type='checkbox' id='color-red' name='red' checked={this.isSelected('RED')} onChange={() => this.toggle('RED')}/>
            <label htmlFor='color-red'><img src='/img/symbols/RED.png' alt='red'/>Red</label>
          </li>
          <li>
            <input type='checkbox' id='color-green' name='green' checked={this.isSelected('GREEN')} onChange={() => this.toggle('GREEN')}/>
            <label htmlFor='color-green'><img src='/img/symbols/GREEN.png' alt='green'/>Green</label>
          </li>
        </ul>
        { this.displayError() }
        { this.displayLoader() }
        <div className='grid three-columns'>
          <div/>
          <input type='submit' id='play-button' value='Play'/>
        </div>
      </form>
    )
  }
}

const mapStateToProps = state => {
  return {
    matagGameUrl: get(state, 'config.matagGameUrl', '')
  }
}

const mapDispatchToProps = () => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(RandomColorDeckForm)