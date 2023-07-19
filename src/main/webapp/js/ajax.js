const ajax = {
  get(url) {
    return fetch(url, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
      },
    });
  }
};

export { ajax };