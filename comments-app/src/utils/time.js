import moment from 'moment'

export function parseTime (value) {
  if (value) {
    return moment.unix(value).format('YYYY-DD-MM hh:mm')
  }
}
