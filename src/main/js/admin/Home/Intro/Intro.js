import React, {Component} from 'react'

export default class Intro extends Component {
  getLinkToSupportEmail() {
    return 'mailto:' + this.props.config.matagSupportEmail
  }

  render() {
    return (
      <section id='intro'>
        <p>
          Welcome to <strong>{this.props.config.matagName}</strong>, a web based version of <strong>Magic: The Gathering</strong> where you can play using just your browser.
        </p>
        <p>
          The game is open source and you can find the code, contribute or contact the creators at <a href='https://github.com/MatagTheGame/matag-the-game' target='_blank'>{this.props.config.matagName}</a> github.
        </p>
        <p>
          We are also available at:
        </p>
        <ul>
          <li><a href={this.getLinkToSupportEmail()}>{this.props.config.matagSupportEmail}</a></li>
          <li><a href="https://discord.com/channels/708016395308236824/708016395799101473">{this.props.config.matagName} Discord Channel</a></li>
        </ul>
        <p>
          (Please note that we are not affiliated in any way with the MTG creators, nor we claim any copyright over their game or art assets.)
        </p>
        <p>
          In case you are the only one online, you can play against yourself by opening two browser sessions (windows/tabs) at this address.
        </p>
      </section>
    )
  }
}
