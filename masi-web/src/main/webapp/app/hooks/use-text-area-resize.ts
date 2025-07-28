import { useEffect } from 'react';

export const useTextAreaResize = () => {
  useEffect(() => {
    let textareas = document.getElementsByTagName('textarea');
    for (let i = 0; i < textareas.length; i++) {
      textareas[i].style.height = textareas[i].scrollHeight + 'px';
      textareas[i].addEventListener('input', (e: any) => {
        e.target!.style.height = textareas[i].scrollHeight + 'px';
      });
    }

    return () => {
      for (let i = 0; i < textareas.length; i++) {
        textareas[i].removeEventListener('input', (e: any) => {
          e.target!.style.height = textareas[i].scrollHeight + 'px';
        });
      }
    };
  }, []);
};
