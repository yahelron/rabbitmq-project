#!/usr/bin/env python
import pika, sys, os
import logging, argparse
from argparse import RawTextHelpFormatter

def main():
    examples = sys.argv[0] + " 5672 -s rabbitmq "
    parser = argparse.ArgumentParser(formatter_class=RawTextHelpFormatter,
                                 description='Run consumer.py',
                                 epilog=examples)
    parser.add_argument('-s', '--server', action='store', dest='server', help='The RabbitMQ server.')

    args = parser.parse_args()
    if args.server == None:
        print "Missing required argument: -s/--server"
        sys.exit(1)

    connection = pika.BlockingConnection(pika.ConnectionParameters(args.server))
    channel = connection.channel()

    channel.queue_declare(queue='pc')

    def callback(ch, method, properties, body):
        print(" [x] Received %r" % body)

    channel.basic_consume(queue='pc', on_message_callback=callback, auto_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)