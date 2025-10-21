import { marked } from 'marked'
import DOMPurify from 'dompurify'

export const parseMdToHtmlAndSanitize = (content) => {
  if (!content) {
    return ''
  }
  const unsafeHtml = marked.parse(content)
  return DOMPurify.sanitize(unsafeHtml)
}
