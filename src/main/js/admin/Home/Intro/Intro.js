import React from 'react'

export default function Intro({config}) {
  const getLinkToSupportEmail = () => {
    return 'mailto:' + config.matagSupportEmail
  }

  return (
    <section id='intro'>
      <p>
        Welcome to <strong>{config.matagName}</strong>, a web based version of <strong>Magic: The Gathering</strong> where you can play using just your browser.
      </p>
      <p>
        The game is open source and you can find the code, contribute or contact the creators at <a href='https://github.com/MatagTheGame/matag-the-game' target='_blank'>{config.matagName}</a> github.
      </p>
      <p>
        We are also available at:
      </p>
      <ul>
        <li><a href={getLinkToSupportEmail()}>{config.matagSupportEmail}</a></li>
        <li><a href="https://discord.com/channels/708016395308236824/708016395799101473" target='_blank'>{config.matagName} Discord Channel</a></li>
      </ul>
      <p>
        <u>Most probably you are the only one online</u>, you can play against yourself by opening two browser sessions (windows/tabs) at this address
        and logging in on the second tab with guest.<br />
        Or even better let someone know about this amazing website and play in here with them!
      </p>
    </section>
  )
}
